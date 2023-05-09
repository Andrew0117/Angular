package org.service.user;

import org.service.user.security.SecurityConfiguration;
import org.service.user.service.OsobaService;
import org.service.user.vm.OsobaVM;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "org.service.user")
@EnableJpaRepositories(basePackages = "org.service.user.repositories")
@Import({SecurityConfiguration.class})
public class Application {

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(OsobaService osobaService) {
        return args -> {
            File file = new File(".");
            byte[] bytes;

            File fileEinstein = new File(file.getAbsolutePath() + "/photo/Einstein.jpg");
            try (FileInputStream fileInputStream = new FileInputStream(fileEinstein)) {
                bytes = new byte[(int) fileEinstein.length()];
                fileInputStream.read(bytes);
            }
            OsobaVM osobaVM = new OsobaVM();
            osobaVM.setId(1L);
            osobaVM.setName("Albert");
            osobaVM.setSurname("Einstein");
            osobaVM.setHomePhone("+38 098 242-0008");
            osobaVM.setOfficePhone("+38 098 242-0005");
            osobaVM.setEmail("test@test.org");
            osobaVM.setPhotoOfAPerson(bytes);
            osobaService.save(osobaVM);

            File fileArmstrong = new File(file.getAbsolutePath() + "/photo/Armstrong.jpg");
            try (FileInputStream fileInputStream = new FileInputStream(fileArmstrong)) {
                bytes = new byte[(int) fileArmstrong.length()];
                fileInputStream.read(bytes);
            }
            osobaVM = new OsobaVM();
            osobaVM.setId(2L);
            osobaVM.setName("Luis");
            osobaVM.setSurname("Armstrong");
            osobaVM.setHomePhone("+38 098 242-0001");
            osobaVM.setOfficePhone("+38 098 242-0003");
            osobaVM.setEmail("test1@test.org");
            osobaVM.setPhotoOfAPerson(bytes);
            osobaService.save(osobaVM);

            File fileCurie = new File(file.getAbsolutePath() + "/photo/Curie.jpg");
            try (FileInputStream fileInputStream = new FileInputStream(fileCurie)) {
                bytes = new byte[(int) fileCurie.length()];
                fileInputStream.read(bytes);
            }
            osobaVM = new OsobaVM();
            osobaVM.setId(3L);
            osobaVM.setName("Marie");
            osobaVM.setSurname("Curie");
            osobaVM.setHomePhone("+38 098 242-0009");
            osobaVM.setOfficePhone("+38 098 242-0007");
            osobaVM.setEmail("test2@test.org");
            osobaVM.setPhotoOfAPerson(bytes);
            osobaService.save(osobaVM);
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
