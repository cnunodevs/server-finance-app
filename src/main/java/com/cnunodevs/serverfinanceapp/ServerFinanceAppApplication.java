package com.cnunodevs.serverfinanceapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.entity.enums.AuthorityEnum;
import com.cnunodevs.serverfinanceapp.model.entity.enums.Expresion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PeriodoPresupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PlazoInversion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.SectorActivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoActivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoImporte;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoMovimiento;


@SpringBootApplication
public class ServerFinanceAppApplication implements CommandLineRunner {

	@Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ServerFinanceAppApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        Usuario usuario = Usuario.builder()
                                 .username("user@gmail.com")
                                 .password(passwordEncoder.encode("12345"))
                                 .authority(AuthorityEnum.ROLE_USUARIO)
                                 .enabled(true)
                                 .accountNonExpired(true)
                                 .accountNonLocked(true)
                                 .credentialsNonExpired(true)
                                 .build();
                                 
        Objetivo objetivo = Objetivo.builder()
                                    .nombre("objetivosample")
                                    .descripcion("hola")
                                    .fechaEstimada(LocalDateTime.parse("21-04-2023", DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                                    .monto(BigDecimal.valueOf(23L))
                                    .usuario(usuario)
                                    .build();

        Ahorro ahorro = Ahorro.builder()
                                .nombre("Mi Ahorro")
                                .descripcion("Descripción de mi Ahorro")
                                .tipo(TipoAhorro.LARGO_PLAZO)
                                .importe(new BigDecimal("0"))
                                .automatico(true)
                                .objetivo(objetivo)
                                .usuario(usuario)
                                .build();

        Condicion condicion = Condicion.builder()
                                .expresion(Expresion.DESCONTAR_MAYOR_IGUAL_A)
                                .importe(new BigDecimal("250"))
                                .cantidadDescontar(8L)
                                .tipoImporte(TipoImporte.EFECTIVO)
                                .enabled(true)
                                .ahorro(ahorro)
                                .build();

        ahorro.setCondicion(condicion);
        
        Presupuesto presupuesto = Presupuesto.builder()
                                    .nombre("Mi presupuesto")
                                    .descripcion("Descripción de mi presupuesto")
                                    .periodo(PeriodoPresupuesto.MENSUAL)
                                    .usuario(usuario)
                                    .build();

        Movimiento movimiento = Movimiento.builder()
                                    .importe(new BigDecimal("100.50"))
                                    .tipo(TipoMovimiento.INGRESO)
                                    .concepto("Pago mensual")
                                    .logoConcepto("logo.png")
                                    .usuario(usuario)
                                    .presupuesto(presupuesto)
                                    .contabilizable(true)
                                    .build();

        presupuesto.setMovimientos(Set.of(movimiento));

        Portafolio portafolio = Portafolio.builder()
                        .nombre("Mi Portafolio")
                        .descripcion("Descripción de mi portafolio")
                        .usuario(usuario)
                        .objetivo(objetivo)
                        .build();

        Inversion inversion = Inversion.builder()
                        .nombre("nombre de la inversion")
                        .descripcion("descripcion de la inversion")
                        .precio(new BigDecimal("100.00"))
                        .cantidad(10.0)
                        .plazo(PlazoInversion.LARGO)
                        .perfilRiesgo(PerfilRiesgo.BAJO)
                        .tipo(TipoActivo.ACCIONES)
                        .sector(SectorActivo.TECNOLOGICO)
                        .rentabilidadEsperada(new BigDecimal("8.00"))
                        .simulada(false)
                        .build();

        portafolio.setInversiones(Set.of(inversion));

    }

}
