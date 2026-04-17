package PlanetMel.service;

import PlanetMel.domain.Role;
import PlanetMel.domain.UserAccount;
import PlanetMel.repo.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public UserAccount save(UserAccount account) {
        return userAccountRepository.save(account);
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    public void deleteById(Long id) {
        userAccountRepository.deleteById(id);
    }

    public boolean usernameExists(String username) {
        return userAccountRepository.findByUsername(username) != null;
    }

    // Checks if the master account has already been created
    public boolean isInitialized() {
        return userAccountRepository.count() > 0;
    }

    // Creates the first master/admin account
    public void createMaster(String username, String password) {
        UserAccount master = new UserAccount(
                username,
                Role.MASTER,
                PasswordUtil.hashPassword(password),
                false
        );
        userAccountRepository.save(master);
    }

    // Creates a role account (BM, NA, LOB)
    public void createUser(String username, String password, Role role) {
        UserAccount user = new UserAccount(
                username,
                role,
                PasswordUtil.hashPassword(password),
                true // must change password on first login
        );
        userAccountRepository.save(user);
    }

    // Authenticates a user by username and password
    public UserAccount authenticate(String username, String password) {
        UserAccount account = userAccountRepository.findByUsername(username);
        if (account == null) return null;
        if (!PasswordUtil.verify(password, account.getPasswordHash())) return null;
        return account;
    }

    // Changes password after verifying the current one
    public void changePassword(String username, String currentPassword, String newPassword) {
        UserAccount account = userAccountRepository.findByUsername(username);
        if (account == null) throw new IllegalArgumentException("User not found.");
        if (!PasswordUtil.verify(currentPassword, account.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        account.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userAccountRepository.save(account);
    }

    // Count methods for MasterProvisionController
    public long businessManagersCount() {
        return userAccountRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.BUSINESS_MANAGER).count();
    }

    public long networkAdministratorsCount() {
        return userAccountRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.NETWORK_ADMINISTRATOR).count();
    }

    public long lineOfBusinessesCount() {
        return userAccountRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.LINE_OF_BUSINESS_EXECUTIVE).count();
    }
}