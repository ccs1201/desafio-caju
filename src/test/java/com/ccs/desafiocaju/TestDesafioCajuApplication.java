package com.ccs.desafiocaju;

import org.springframework.boot.SpringApplication;

public class TestDesafioCajuApplication {

	public static void main(String[] args) {
		SpringApplication.from(DesafioCajuApplication::main).with(PostgresConfiguration.class).run(args);
	}

}
