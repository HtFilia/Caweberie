package models;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Entity
public class User extends Model {

    /**
     * @brief The email of the user.
     */
    @Email
    @Required
    public String email;

    /**
     * @brief The hash of the user's password.
     */
    @Required
    public String hash;

    /**
     * @brief The random salt generated at User's creation.
     */
    @Required
    public String salt;

    /**
     * @brief The username displayed to others.
     */
    @Required
    public String username;

    //TODO: Ajouter les forums modérateurs et participants

    /**
     * @brief Boolean that specifies if a username is an admin or regular user.
     */
    public boolean isAdmin;

    public User(String email, String password, String username) {
        this.email = email;
        this.salt = getRandomSalt();
        this.hash = getCorrespondingHash(password, salt);
        this.username = username;
        this.isAdmin = false;
    }

    /**
     * Connect to a desired User if the email and password combination is valid.
     * @param email the email of the desired User.
     * @param password the password of the desired User.
     * @return the desired User if the combination is valid, null otherwise.
     */
    public static User connect(String email, String password) {
        // Get user that corresponds to this email
        User emailUser = find("byEmail", email).first();
        // If no User exists with said email
        if (emailUser == null) {
            return null;
        }
        // Generate hash with input's password and emailUser salt
        String generatedHash = getCorrespondingHash(password, emailUser.salt);
        // If generatedHash corresponds to the hash in DB then connect
        if (generatedHash.equals(emailUser.hash)) {
            return emailUser;
        }
        // Otherwise refuse connection
        return null;
    }

    /**
     * Generate a random salt of length 10.
     * @return a random salt of length 10 with alphanumerical characters.
     */
    private static String getRandomSalt() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Get the corresponding hash for a specified password and salt
     * @param password the password gotten through user's input
     * @param salt a salt used with the password through the hash algorithm
     * @return the hash of the password
     */
    private static String getCorrespondingHash(String password, String salt) {
        String generatedHash = null;
        try {
            // Get encryption's algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // Add salt to encryption's algorithm
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            // Add user's password input to encryption's algorithm
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Get corresponding hash
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // We keep only the first 60 characters
        return generatedHash.substring(0, 60);
    }

    /**
     * A User is defined through its username.
     * @return the username of an User.
     */
    @Override
    public String toString() {
        return username;
    }
}
