package com.fieldright.fr.util.task;

import com.fieldright.fr.repository.DataBaseConnector;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

/**
 * Essa task é responsável para desativar todos os vendedores com data de desativação menor do que a data atual.
 * Ela será executada todos os dias as 00h00, hora do servidor.
 */
public class DesativaVendedoresTask extends TimerTask {

    private static final String QUERY =
            "update usuario " +
                    "    set active = false " +
                    "        where dtype = 'Vendedor' " +
                    "            and active = true " +
                    "            and proxima_desativacao is not null " +
                    "            and proxima_desativacao < current_date ";

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("EXECUTANDO TAREFA : VALIDAÇÃO DATA DE DESATIVAÇÃO DE VENDEDORES...");
        Connection connection = new DataBaseConnector().connect();
        Statement statement = null;

        if (connection != null) {
            System.out.println("Conectado ao banco de dados com sucesso!");
            try {
                statement = connection.createStatement();
                statement.execute(QUERY);
                System.out.println("TAREFA EXECUTADA COM SUCESSO : VALIDAÇÃO DATA DE DESATIVAÇÃO DE VENDEDORES");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                statement.close();
            }
        }

    }
}
