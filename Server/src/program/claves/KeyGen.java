package program.claves;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Clase que nos permite generar par de claves
 */
public class KeyGen {
    /**
     * Genera una clave pública y privada
     *
     * @return par de claves
     */
    public static KeyPair generarClaves() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Tipo de algoritmo
            KeyPairGenerator generar = KeyPairGenerator.getInstance("RSA", "BC");
            generar.initialize(2048);
            return generar.genKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Desencripta un mensaje con la clave privada proporcionada
     *
     * @param mensajeCifrado mensaje cifrado
     * @param clavePrivada   clave privada para descifrar el mensaje
     * @return mensaje descifrado
     */
    public static String descifrar(byte[] mensajeCifrado, PrivateKey clavePrivada) {
        try {
            // Es importante encirptar y desencriptar con la misma tranformacion y provider
            // En este caso el provider es BouncyCastleProvider implementado por mi
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
            return new String(cipher.doFinal(mensajeCifrado));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}
