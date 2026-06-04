/**
 * Class/Interface: UserSession
 * Creator: Keweil Anak Bansa
 * Tester: G04/SE Group 14
 * Description: Defines login session behaviours for the Healiverse app.
 * Implemented by: LoginApplication
 */

public interface UserSession {
    String getUsername();

    boolean isLoggedIn();

    void logout();
}
