/**
 * Interface: UserSession
 * Creator: Keweil Anak Bansa
 * Tester: G04/SE Group 14
 * Purpose: Defines login session behaviours for the Healiverse app.
 * Implemented by: LoginApplication
 */
public interface UserSession {
    String getUsername();

    boolean isLoggedIn();

    void logout();
}