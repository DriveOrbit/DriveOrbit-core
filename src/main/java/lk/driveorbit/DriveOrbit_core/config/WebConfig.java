package lk.driveorbit.DriveOrbit_core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${qrcode.upload.dir:upload/qrcodes}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // QR code directory
        Path qrcodesUploadDir = Paths.get(uploadDir);
        String qrcodesUploadPath = qrcodesUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/qrcodes/**")
                .addResourceLocations("file:" + qrcodesUploadPath + "/");
        
        // Static resources
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
