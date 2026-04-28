package com.riwi.talent.view;

import com.riwi.talent.controller.EmpleadoController;
import com.riwi.talent.model.DatabaseConfig;
import com.riwi.talent.model.EmpleadoDAO;
import com.riwi.talent.model.JdbcEmpleadoDAO;

/**
 * Punto de entrada para la aplicacion MVC en consola.
 */
public final class TalentApp {

    private TalentApp() {
    }

    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();
        EmpleadoDAO empleadoDAO = new JdbcEmpleadoDAO(config);
        EmpleadoController controller = new EmpleadoController(empleadoDAO);
        TalentConsoleView view = new TalentConsoleView(controller);
        view.iniciar();
    }
}
