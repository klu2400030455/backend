package com.tribal.crafts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serves locally uploaded files over HTTP.
 *
 * FileStorageService saves files to:
 *   <working-dir>/uploads/products/<uuid>.jpg   (uploadDir = "uploads/products")
 *
 * This handler maps the URL  /uploads/**  →  <working-dir>/uploads/
 * so that /uploads/products/<uuid>.jpg resolves correctly.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    // The sub-directory where files are written, e.g. "uploads/products"
    @Value("${file.upload-dir:uploads/products}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Derive the root "uploads/" folder from the configured sub-directory.
        // e.g.  "uploads/products"  →  parent  →  "uploads"
        Path uploadSubDir  = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path uploadRootDir = uploadSubDir.getParent(); // → .../uploads

        // Convert to a file:// URI that Spring's resource handler understands.
        // Must end with "/" for Spring MVC to treat it as a directory.
        String resourceLocation = uploadRootDir.toUri().toString();
        if (!resourceLocation.endsWith("/")) {
            resourceLocation += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
