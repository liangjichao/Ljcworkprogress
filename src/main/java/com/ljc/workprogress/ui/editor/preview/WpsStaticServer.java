package com.ljc.workprogress.ui.editor.preview;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.ide.BuiltInServerManager;
import org.jetbrains.ide.HttpRequestHandler;
import org.jetbrains.io.FileResponses;
import org.jetbrains.io.Responses;

import java.util.Date;
import java.util.HashMap;

public class WpsStaticServer extends HttpRequestHandler {

    private static final String prefixUuid = "da2f7518-aefb-4187-9792-574c65473521";
    private static final String prefixPath = "/" + prefixUuid;

    private static ResourceProvider defaultResourceProvider = new DefaultResourceProvider();
    private static HashMap<Integer, ResourceProvider> resourceProviders = Maps.newHashMap();

    static {
        resourceProviders.put(defaultResourceProvider.hashCode(), defaultResourceProvider);
    }

    public static WpsStaticServer getInstance() {
        return WpsStaticServer.Companion.getEP_NAME().findExtension(WpsStaticServer.class);
    }

    public static String getStaticUrl(ResourceProvider resourceProvider, String staticPath) {
        int providerHash = resourceProvider.hashCode();
        int port = BuiltInServerManager.getInstance().getPort();
        String raw = String.format("http://localhost:%s/%s/%s/%s", port, prefixUuid, providerHash, staticPath);
        Url url = Urls.parseEncoded(raw);
        if (url == null) {
            throw new RuntimeException("Could not parse url!");
        }
        return BuiltInServerManager.getInstance().addAuthToken(url).toExternalForm();
    }

    public synchronized Disposable registerResourceProvider(ResourceProvider resourceProvider) {
        resourceProviders.put(resourceProvider.hashCode(), resourceProvider);
        return new Disposable() {
            @Override
            public void dispose() {
                unregisterResourceProvider(resourceProvider);
            }
        };
    }

    public synchronized void unregisterResourceProvider(ResourceProvider resourceProvider) {
        resourceProviders.remove(resourceProvider.hashCode());
    }

    private Integer getProviderHash(String path) {
        String[] split = path.split("/");
        if (split.length >= 2) {
            return NumberUtils.toInt(split[2]);
        }
        return null;
    }

    private String getStaticPath(String path) {

        String[] split = path.split("/");
        if (split.length >= 3) {
            String[] arr = ArrayUtils.subarray(split, 3, split.length);
            return Joiner.on("/").join(arr);

        }
        return "";
    }

    private ResourceProvider obtainResourceProvider(String path) {
        Integer providerHash = getProviderHash(path);
        synchronized (resourceProviders) {
            return resourceProviders.getOrDefault(providerHash, null);
        }

    }

    @Override
    public boolean isSupported(FullHttpRequest request) {
        if (!super.isSupported(request)) {
            return false;
        }
        String path = request.uri();
        return path.startsWith(prefixPath);
    }

    @Override
    public boolean process(QueryStringDecoder urlDecoder, FullHttpRequest request, ChannelHandlerContext context) {
        String path = urlDecoder.path();

        ResourceProvider resourceProvider = obtainResourceProvider(path);
        if (resourceProvider == null) {
            return false;
        }
        String resourceName = getStaticPath(path);
        if (resourceProvider.canProvide(resourceName)) {
            sendResource(request, context.channel(), resourceProvider.loadResource(resourceName), resourceName);
            return true;
        }
        return false;
    }

    private String[] typesForExplicitUtfCharset = new String[]{"application/javascript", "text/html", "text/css", "image/svg+xml"};

    private String guessContentType(String resourceName) {
        String type = FileResponses.INSTANCE.getContentType(resourceName);
        if (ArrayUtils.contains(typesForExplicitUtfCharset, type)) {
            return String.format("%s; charset=utf-8", type);
        }
        return type;
    }

    private void sendResource(HttpRequest request, Channel channel, Resource resource, String resourceName) {
        long lastModified = ApplicationInfo.getInstance().getBuildDate().getTimeInMillis();

        if (FileResponses.INSTANCE.checkCache(request, channel, lastModified)) {
            return;
        }
        if (resource == null) {
            Responses.send(HttpResponseStatus.NOT_FOUND, channel, request);
            return;
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(resource.getContent()));
        if (resource.getType() != null) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, resource.getType());
        } else {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, guessContentType(resourceName));
        }
        response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, must-revalidate");
        response.headers().set(HttpHeaderNames.LAST_MODIFIED, new Date(lastModified));
        Responses.send(response, channel, request);

    }
}
