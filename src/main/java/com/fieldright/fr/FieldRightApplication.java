package com.fieldright.fr;
import org.springframework.context.annotation.Bean;
import com.fieldright.fr.util.task.DesativaVendedoresTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Calendar;
import java.util.Timer;

@SpringBootApplication
public class FieldRightApplication {

   

    public static void main(String[] args) {
        SpringApplication.run(FieldRightApplication.class, args);
        agendaTaskDesativacaoVendedores();
    }

    private static void agendaTaskDesativacaoVendedores() {
        Calendar calendar = getDiaSeguinteMeiaNoite();
        Timer timer = new Timer();
        timer.schedule(new DesativaVendedoresTask(), calendar.getTime(), 86400000);
    }

    private static Calendar getDiaSeguinteMeiaNoite() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    @Bean
	public WebMvcConfigurer configure() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins("*");
			}
			
		};
	}

}
