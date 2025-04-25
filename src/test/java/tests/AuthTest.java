package tests;

import exceptions.AuthenticationException;
import models.Applicant;
import models.User;
import models.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import repositories.UserRepository;
import services.AuthService;
import utils.Hash;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

class AuthTest {

    MockedStatic<UserRepository> mockedUserRepo;
    MockedStatic<Hash> mockedHash;
    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = AuthService.getInstance();
        mockedUserRepo = Mockito.mockStatic(UserRepository.class, Mockito.withSettings().lenient());
        mockedHash = Mockito.mockStatic(Hash.class, Mockito.withSettings().lenient());
    }

    @AfterEach
    void tearDown() {
        mockedUserRepo.close();
        mockedHash.close();
    }

    @Test
    @DisplayName("Login successful with correct applicant credentials")
    void login_ValidApplicantCredentials_ReturnsUserAndSetsActive() throws AuthenticationException {
        String nric = "S1234567A";
        String password = "password";
        String expectedHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        Applicant mockUserFromRepo = new Applicant(nric, "Alice Applicant", expectedHashedPassword, 30, null);

        mockedUserRepo.when(() -> UserRepository.getByNRIC(nric)).thenReturn(mockUserFromRepo);

        mockedHash.when(() -> Hash.verifyPassword(password, expectedHashedPassword)).thenReturn(true);

        mockedUserRepo.when(() -> UserRepository.setActiveUser(any(User.class))).thenAnswer(invocation -> null);

        User loggedInUser = authService.login(nric, password);

        assertNotNull(loggedInUser, "Logged in user should not be null on success");
        assertEquals(nric, loggedInUser.getUserNRIC(), "NRIC should match");
        assertEquals("Alice Applicant", loggedInUser.getName(), "Name should match");
        assertEquals(Role.APPLICANT, loggedInUser.getRole(), "Role should match");

        mockedUserRepo.verify(() -> UserRepository.getByNRIC(nric));
        mockedHash.verify(() -> Hash.verifyPassword(password, expectedHashedPassword));
        mockedUserRepo.verify(() -> UserRepository.setActiveUser(mockUserFromRepo));
    }

    @Test
    @DisplayName("Login fails with incorrect password")
    void login_IncorrectPassword() {
        // Arrange
        String nric = "S1234567A";
        String wrongPassword = "wrong";
        String expectedCorrectHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        User mockUserFromRepo = new User(nric, "Test User", expectedCorrectHash,30);

        mockedUserRepo.when(() -> UserRepository.getByNRIC(nric)).thenReturn(mockUserFromRepo);

        mockedHash.when(() -> Hash.verifyPassword(wrongPassword, expectedCorrectHash)).thenReturn(false);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.login(nric, wrongPassword);
        }, "AuthenticationException should be thrown for incorrect password");

        assertEquals("Invalid NRIC or password", exception.getMessage(), "Exception message should indicate invalid password");

        mockedUserRepo.verify(() -> UserRepository.getByNRIC(nric));
        mockedHash.verify(() -> Hash.verifyPassword(wrongPassword, expectedCorrectHash));
        mockedUserRepo.verify(() -> UserRepository.setActiveUser(any(User.class)), never());
    }

    @Test
    @DisplayName("Login fails with incorrect NRIC")
    void login_UserNotFound() {
        String nric = "S9999999Z";
        String password = "password";
        String expectedHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        User mockUserFromRepo = new User(nric, "Test User", expectedHashedPassword, 30);

        mockedUserRepo.when(() -> UserRepository.getByNRIC(nric)).thenReturn(null);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.login(nric, password);
        }, "AuthenticationException should be thrown for incorrect NRIC");

        assertEquals("Invalid NRIC or password", exception.getMessage(), "Exception message should indicate incorrect NRIC");

        mockedUserRepo.verify(() -> UserRepository.getByNRIC(nric));
        mockedHash.verifyNoInteractions();
        mockedUserRepo.verify(() -> UserRepository.setActiveUser(any(User.class)), never());
    }

    @Test
    @DisplayName("Login successful after password change")
    void login_PasswordChange_SuccessfulLogin() throws AuthenticationException {
        String nric = "S1234567A";
        String oldPassword = "oldpassword";
        String hashedOldPassword = "f0a5cdf5a9b255d3a71acdee7bd29c6b320f27e71f105b86220696f21b67c6e9";
        String newPassword = "password";
        String hashedNewPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        User mockUserFromRepo = new Applicant(nric, "PC Applicant", hashedOldPassword, 30, null);

        mockedHash.when(() -> Hash.verifyPassword(oldPassword, hashedOldPassword)).thenReturn(true);
        authService.changePassword(mockUserFromRepo, oldPassword, newPassword);
        mockedUserRepo.when(() -> UserRepository.getByNRIC(nric)).thenReturn(
                new Applicant(nric, "PC Applicant", hashedNewPassword, 30, null)
        );
        mockedHash.when(() -> Hash.verifyPassword(newPassword, hashedNewPassword)).thenReturn(true);
        mockedUserRepo.when(() -> UserRepository.setActiveUser(any(User.class))).thenAnswer(invocation -> null);
        User loggedInUser = authService.login(nric, newPassword);

        assertNotNull(loggedInUser, "Logged in user should not be null on success");
        assertEquals(hashedNewPassword, loggedInUser.getPassword(), "Password should be updated");
        assertEquals("PC Applicant", loggedInUser.getName(), "Name should match");
        assertEquals(Role.APPLICANT, loggedInUser.getRole(), "Role should match");

        mockedUserRepo.verify(() -> UserRepository.getByNRIC(nric));
        mockedHash.verify(() -> Hash.verifyPassword(newPassword, hashedNewPassword));
        mockedUserRepo.verify(() -> UserRepository.setActiveUser(any(Applicant.class)));
    }
}
