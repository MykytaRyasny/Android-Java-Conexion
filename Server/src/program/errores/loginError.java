package program.errores;

public class loginError extends Exception {
    private String mensajeError;

    /**
     * Constructor con el error que queramos mostrar y el error original
     * para no perder el resto del mesnaje del error
     *
     * @param mensajeError el mensaje que se va a imprimir
     */

    public loginError(String mensajeError) {
        super(mensajeError);
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
