package program.Claves;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

// Clase que nos va a generar clave publica y privada
public class KeyGen {
    /**
     * Genera una clave pública y privada
     * @return par de claves
     */
    public static KeyPair generarClaves(){
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Tipo de algoritmo
            KeyPairGenerator generar = KeyPairGenerator.getInstance("RSA", "BC");
            generar.initialize(2048);
            return generar.genKeyPair();
        } catch (GeneralSecurityException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Desencripta un mensaje con la clave privada proporcionada
     * @param mensajeEncriptado mensaje encriptado
     * @param clavePrivada clave privada para desencriptar el mensaje
     * @return mensaje desencriptado
     */
    public static String descifrar (byte[] mensajeEncriptado, PrivateKey clavePrivada){
        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
            return new String(cipher.doFinal(mensajeEncriptado));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}
