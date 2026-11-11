package br.com.fiap.mscontribuicoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsContribuicoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsContribuicoesApplication.class, args);
	}

}
