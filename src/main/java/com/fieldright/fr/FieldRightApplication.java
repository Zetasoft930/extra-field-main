package com.fieldright.fr;
import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.repository.ContaRepository;
import com.fieldright.fr.service.interfaces.HistoricoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.fieldright.fr.util.task.DesativaVendedoresTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class FieldRightApplication implements CommandLineRunner {


    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private HistoricoPagamentoService historicoPagamentoService;
   

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

    @Override
    public void run(String... args) throws Exception {

/*
      List<Conta> lista  =  contaRepository.findAll();

      for(Conta c : lista)
      {

          Random r = new Random();
          c.setSaldo(c.getSaldo().add(new BigDecimal(r.nextDouble())));
          boolean isAutorizado = true;
          String trasnferenciCode = String.valueOf(r.nextInt(1000000));

          this.historicoPagamentoService.saveOrUpdate(c,isAutorizado,trasnferenciCode);

      }*/


    }
}
