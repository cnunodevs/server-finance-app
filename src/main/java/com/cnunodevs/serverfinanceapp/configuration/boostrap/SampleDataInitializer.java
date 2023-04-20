package com.cnunodevs.serverfinanceapp.configuration.boostrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
import com.cnunodevs.serverfinanceapp.service.AhorrosService;
import com.cnunodevs.serverfinanceapp.service.ObjetivoService;
import com.cnunodevs.serverfinanceapp.service.PortafoliosService;
import com.cnunodevs.serverfinanceapp.service.PresupuestosService;
import com.cnunodevs.serverfinanceapp.service.UsuariosService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SampleDataInitializer implements CommandLineRunner {

    private final UsuariosService usuariosService;
    private final ObjetivoService objetivoService;
    private final AhorrosService ahorrosService;
    private final PresupuestosService presupuestosService;
    private final PortafoliosService portafolioService;


    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(!usuariosService.usernameAlreadyExist("test@gmail.com")){
            loadSampleData();
        }
    }

    private void loadSampleData(){
        
        Usuario usuario = loadAdminData();
        Objetivo objetivo = loadObjetivoData(usuario);
        loadAhorroData(usuario, objetivo);
        loadPresupuestoData(usuario);
        loadPortafolio(usuario, objetivo);
    }

    private Usuario loadAdminData(){
        //Load test user
        Usuario usuario = Usuario.builder()
                                 .username("test@gmail.com")
                                 .password(passwordEncoder.encode("12345"))
                                 .authority(AuthorityEnum.ROLE_USUARIO)
                                 .enabled(true)
                                 .accountNonExpired(true)
                                 .accountNonLocked(true)
                                 .credentialsNonExpired(true)
                                 .build();
        return usuariosService.createUsuario(usuario);
    }
    
    private Objetivo loadObjetivoData(Usuario usuario){
        Objetivo objetivo = Objetivo.builder()
                                    .nombre("objetivosample")
                                    .descripcion("hola")
                                    .fechaEstimada(LocalDateTime.now())
                                    .monto(BigDecimal.valueOf(10000000.00))
                                    .usuario(usuario)
                                    .build();
        return objetivoService.saveObjetivo(objetivo);
    }

    private Ahorro loadAhorroData(Usuario usuario, Objetivo objetivo){
        Ahorro ahorro = Ahorro.builder()
                                .nombre("Mi Ahorro")
                                .descripcion("Descripción de mi Ahorro")
                                .tipo(TipoAhorro.LARGO_PLAZO)
                                .importe(new BigDecimal(0))
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
        return ahorrosService.createBolsilloAhorro(ahorro);
        
    }

    private Presupuesto loadPresupuestoData(Usuario usuario) {
        Presupuesto presupuesto = Presupuesto.builder()
                                            .nombre("Mi presupuesto")
                                            .descripcion("Descripción de mi presupuesto")
                                            .periodo(PeriodoPresupuesto.MENSUAL)
                                            .usuario(usuario)
                                            .build();

        
        presupuesto.setMovimientos(Set.of(
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.INGRESO)
                            .concepto("Transporte")
                            .logoConcepto("assets/svg/logo-concepto/transporte.svg")
                            .usuario(usuario)
                            .presupuesto(null)
                            .contabilizable(true)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.INGRESO)
                            .concepto("Rutina")
                            .logoConcepto("assets/svg/logo-concepto/rutina.svg")
                            .usuario(usuario)
                            .presupuesto(presupuesto)
                            .contabilizable(false)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.INGRESO)
                            .concepto("Familia")
                            .logoConcepto("assets/svg/logo-concepto/familia.svg")
                            .usuario(usuario)
                            .presupuesto(null)
                            .contabilizable(true)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.INGRESO)
                            .concepto("Alimentacion")
                            .logoConcepto("assets/svg/logo-concepto/alimentacion.svg")
                            .usuario(usuario)
                            .presupuesto(presupuesto)
                            .contabilizable(false)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.INGRESO)
                            .concepto("Regalo")
                            .logoConcepto("assets/svg/logo-concepto/regalo.svg")
                            .usuario(usuario)
                            .presupuesto(null)
                            .contabilizable(true)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.EGRESO)
                            .concepto("Intereses")
                            .logoConcepto("assets/svg/logo-concepto/intereses.svg")
                            .usuario(usuario)
                            .presupuesto(presupuesto)
                            .contabilizable(false)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.EGRESO)
                            .concepto("Salario")
                            .logoConcepto("assets/svg/logo-concepto/salario.svg")
                            .usuario(usuario)
                            .presupuesto(null)
                            .contabilizable(true)
                            .build(),
            Movimiento.builder()
                            .importe(new BigDecimal("100.50"))
                            .tipo(TipoMovimiento.EGRESO)
                            .concepto("Otros")
                            .logoConcepto("assets/svg/logo-concepto/otros.svg")
                            .usuario(usuario)
                            .presupuesto(presupuesto)
                            .contabilizable(false)
                            .build()
        ));
        
        return presupuestosService.createPresupuesto(presupuesto);
    }

    private Portafolio loadPortafolio(Usuario usuario, Objetivo objetivo) {
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
        return portafolioService.createPortafolio(portafolio);
    }

}
