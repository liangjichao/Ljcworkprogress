package com.jdl.ljc.joyworkprogress

import org.jetbrains.annotations.ApiStatus
import java.io.File
import kotlin.reflect.KClass

/**
 *
 * @author liangjichao
 * @date 2023/10/27 3:43 PM
 */
interface ResourceProvider {
    class Resource(
            val content: ByteArray,
            val type: String? = null
    )

    /**
     * @return true if this resource provider can load resource [resourceName]
     * with it's [loadResource] method.
     */
    fun canProvide(resourceName: String): Boolean

    /**
     * Load [resourceName] contents.
     *
     * @param resourceName Resource path.
     * @return [Resource] if resource was successfully loaded or null if load failed.
     */
    fun loadResource(resourceName: String): ResourceProvider.Resource?

    /**
     * Default resource provider implementation with
     * [canProvide] and [loadResource] returning always false and null.
     */
    class DefaultResourceProvider : ResourceProvider {
        override fun canProvide(resourceName: String): Boolean = false
        override fun loadResource(resourceName: String): ResourceProvider.Resource? = null
    }

    companion object {
        /**
         * Shared instance of [DefaultResourceProvider].
         */
        val default: ResourceProvider = DefaultResourceProvider()

        @JvmStatic
        fun <T> loadInternalResource(cls: Class<T>, path: String, contentType: String? = null): ResourceProvider.Resource? {
            return cls.getResourceAsStream(path)?.use {
                Resource(it.readBytes(), contentType)
            }
        }

        /**
         * Load resource using [cls]'s [ClassLoader].
         *
         * @param cls Kotlin class to get the [ClassLoader] of.
         * @param path Path of the resource to load.
         * @param contentType Explicit type of content. If null, [PreviewStaticServer] will
         * try to guess content type based on resource name. See [loadInternalResource].
         * @return [Resource] with the contents of resource, or null in case
         * the resource could not be loaded.
         */
        @JvmStatic
        fun <T : Any> loadInternalResource(cls: KClass<T>, path: String, contentType: String? = null): ResourceProvider.Resource? {
            return loadInternalResource(cls.java, path, contentType)
        }

        /**
         * See [loadInternalResource]
         */
        @JvmStatic
        inline fun <reified T : Any> loadInternalResource(path: String, contentType: String? = null): ResourceProvider.Resource? {
            return loadInternalResource(T::class.java, path, contentType)
        }

        /**
         * Load resource from the filesystem.
         *
         * @param file File to load.
         * @param contentType Explicit type of content. If null, [PreviewStaticServer] will
         * try to guess content type based on resource name. See [loadInternalResource].
         * @return [Resource] with the contents of resource, or null in case
         * the resource could not be loaded.
         */
        @JvmStatic
        fun loadExternalResource(file: File, contentType: String? = null): ResourceProvider.Resource? {
            if (!file.exists()) {
                return null
            }
            val content = file.inputStream().use { it.readBytes() }
            return Resource(content, contentType)
        }

        @ApiStatus.Experimental
        @JvmStatic
        fun createResourceProviderChain(vararg providers: ResourceProvider): ResourceProvider {
            return object : ResourceProvider {
                override fun canProvide(resourceName: String): Boolean {
                    return providers.any { it.canProvide(resourceName) }
                }

                override fun loadResource(resourceName: String): ResourceProvider.Resource? {
                    return providers.firstNotNullOfOrNull { it.loadResource(resourceName) }
                }
            }
        }
    }
}