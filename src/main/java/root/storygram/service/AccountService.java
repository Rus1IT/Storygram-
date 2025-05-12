package root.storygram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.storygram.dto.request.account.AuthenticationRequestDto;
import root.storygram.dto.request.account.UpdatePasswordRequestDto;
import root.storygram.dto.request.account.UpdateUsernameRequestDto;
import root.storygram.dto.response.AccountData;
import root.storygram.entity.Account;
import root.storygram.enums.AccountType;
import root.storygram.enums.FollowingRequestStatus;
import root.storygram.exception.AlreadyExistException;
import root.storygram.exception.NotFoundException;
import root.storygram.mapper.AccountMapper;
import root.storygram.repository.AccountRepository;
import root.storygram.security.SecurityUtil;

import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccountFollowerService accountFollowerService;

    public void createAccount(AuthenticationRequestDto accountDto){
        log.debug("Trying to create an account with username: {} and login: {}",accountDto.getUsername(), accountDto.getLogin());

        validateUniqueField(accountDto.getUsername(), accountRepository::existsByUsername, "username");
        validateUniqueField(accountDto.getLogin(), accountRepository::existsByLogin, "login");

        Account newAccount = accountMapper.accountRequestDtoToAccount(accountDto);
        newAccount.setPassword(passwordEncoder.encode(newAccount.getPassword()));
        accountRepository.save(newAccount);

        log.info("Account {} created",newAccount);
    }

    public AccountData getAccountData(){
        Account account = getCurrentAccount();
        String username = account.getUsername();

        log.debug("Trying to retrieve an account with username: {}", username);

        log.info("Account {} got",username);
        return accountMapper.accountToAccountData(account);
    }

    public void deleteAccount() {
        Account account = getCurrentAccount();
        String username = account.getUsername();

        log.debug("Trying to delete an account with username: {}", username);

        accountRepository.delete(account);

        log.info("Account {} deleted",username);
    }

    public void updateUsername(UpdateUsernameRequestDto usernameRequestDto) {
        final String newUsername = usernameRequestDto.getNewUsername();

        Account account = getCurrentAccount();
        String currentUsername = account.getUsername();

        log.debug("Trying to update an username with: {} to: {}", currentUsername,newUsername);

        validateUniqueField(newUsername, accountRepository::existsByUsername,"New username");
        if (currentUsername.equals(newUsername)) {
            log.warn("The new username is identical to the current one. No update is performed.");
            return;
        }
        account.setUsername(newUsername);
        accountRepository.save(account);
        log.info("Username {} changed to {}", currentUsername,newUsername);
    }

    public void updatePassword(UpdatePasswordRequestDto passwordRequestDto) {
        Account account =getCurrentAccount();
        String username = account.getUsername();

        log.debug("Trying to update a password with username: {}", username);

        String newPassword = passwordRequestDto.getConfirmPassword();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        log.info("Password for {} updated",username);
    }

    public void updateActiveStatus(boolean isActive){
        Account account = getCurrentAccount();
        String username = account.getUsername();

        log.debug("Trying to update aт active status with username: {} isActive: {}", username, isActive);

        account.setActive(isActive);
        accountRepository.save(account);

        log.info("Active status for {} updated to {}",username, isActive);
    }

    public void updateAccountType(AccountType accountType) {
        Account account = getCurrentAccount();

        if (account.getType() != accountType && accountType == AccountType.OPEN) {
            accountFollowerService.acceptAllPendingRequests(account.getProfile());
        }

        account.setType(accountType);
        accountRepository.save(account);
    }


    public Account getCurrentAccount(){
        String username = SecurityUtil.getCurrentUsername();
        return getAccountByUsername(username);
    }

    private Account getAccountByUsername(String username){
       return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("Username %s not found", username),"username"));

    }

    private void validateUniqueField(String value,
                                     Predicate<String> existsCheck,
                                    String fieldName) throws AlreadyExistException {
        if(existsCheck.test(value)){
            throw new AlreadyExistException(String.format("%s %s already exist",fieldName, value), fieldName);
        }
    }


}
