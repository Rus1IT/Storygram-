package root.storygram.mapper;

import org.mapstruct.Mapper;
import root.storygram.dto.request.account.AuthenticationRequestDto;
import root.storygram.dto.response.AccountData;
import root.storygram.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account accountRequestDtoToAccount(AuthenticationRequestDto authorizationRequestDto);
    AccountData accountToAccountData(Account account);
}
