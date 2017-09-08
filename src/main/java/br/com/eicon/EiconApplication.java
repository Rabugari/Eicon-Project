package br.com.eicon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EiconApplication{

	public static final Logger log = LoggerFactory.getLogger(EiconApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EiconApplication.class, args);
	}
}
