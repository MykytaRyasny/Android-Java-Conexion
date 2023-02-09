package program.errores;

import java.sql.SQLException;

// En esta clase crearemos nuestras excepciones si asi lo deseamos
public class registerError extends SQLException {

    private String mensajeError;

    /**
     * Constructor con el error que queramos mostrar y el error original
     * para no perder el resto del mesnaje del error
     * @param mensajeError el mensaje que se va a imprimir
     * @param e El error original
     */

    public registerError(String mensajeError, Throwable e) {
        super(mensajeError, e);
        this.mensajeError = mensajeError;
    }

    /**
     * Getter
     * @return El error del constructor
     */
    @Override
    public String getMessage() {
        return mensajeError;
    }
}