package root.storygram.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import root.storygram.dto.request.account.AuthenticationRequestDto;
import root.storygram.dto.request.account.AuthorizationRequestDto;
import root.storygram.dto.request.account.UpdatePasswordRequestDto;
import root.storygram.dto.request.account.UpdateUsernameRequestDto;
import root.storygram.dto.response.AccountData;
import root.storygram.dto.response.ApiResponse;
import root.storygram.enums.AccountType;
import root.storygram.exception.NotFoundException;
import root.storygram.service.AccountService;
import root.storygram.webtoken.JwtService;


import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody @Valid AuthorizationRequestDto authorizationRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authorizationRequestDto.getAccount(), authorizationRequestDto.getPassword()
                ));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(userDetailsService.loadUserByUsername(authorizationRequestDto.getAccount()));
        }
        throw new NotFoundException(String.format("Username %s not found", authorizationRequestDto.getAccount()),"username");
    }

    @PostMapping
    public ResponseEntity<?> addAccount(@Valid @RequestBody AuthenticationRequestDto authorizationRequestDto) {
        accountService.createAccount(authorizationRequestDto);
        return ResponseEntity.status(CREATED).body(ApiResponse.success(null,"Account created successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getAccount(){
        AccountData accountData = accountService.getAccountData();
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(accountData,"Account retrieved successfully"));
    }

    @PatchMapping("/update/username")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody UpdateUsernameRequestDto updateUsernameRequestDto){
        accountService.updateUsername(updateUsernameRequestDto);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null,"Account updated successfully"));
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto){
        accountService.updatePassword(updatePasswordRequestDto);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null,"Account updated successfully"));
    }

    @PatchMapping("/type")
    public ResponseEntity<?> changeAccountType(@RequestParam("account_type") AccountType accountType){
        accountService.updateAccountType(accountType);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null, "Account type updated successfully"));
    }

    @GetMapping("/update/active-status")
    public ResponseEntity<?> updateActiveStatus(@RequestParam boolean activeStatus){
        accountService.updateActiveStatus(activeStatus);
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null,"Account status updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(){
        accountService.deleteAccount();
        return ResponseEntity.status(OK)
                .body(ApiResponse.success(null,"Account deleted successfully"));
    }

}
