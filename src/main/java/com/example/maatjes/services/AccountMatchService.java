//package com.example.maatjes.services;
//
//import com.example.maatjes.dtos.AccountDto;
//import com.example.maatjes.dtos.MatchDto;
//import com.example.maatjes.exceptions.RecordNotFoundException;
//import com.example.maatjes.models.Account;
//import com.example.maatjes.models.AccountMatch;
//import com.example.maatjes.models.AccountMatchKey;
//import com.example.maatjes.models.Match;
//import com.example.maatjes.repositories.AccountMatchRepository;
//import com.example.maatjes.repositories.AccountRepository;
//import com.example.maatjes.repositories.MatchRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class AccountMatchService {
//
//    private AccountRepository accountRepository;
//
//    private MatchRepository matchRepository;
//
//    private AccountMatchRepository accountMatchRepository;
//
//    public AccountMatchService(AccountRepository accountRepository, MatchRepository matchRepository, AccountMatchRepository accountMatchRepository) {
//        this.accountRepository = accountRepository;
//        this.matchRepository = matchRepository;
//        this.accountMatchRepository = accountMatchRepository;
//    }
//
//    public Collection<AccountDto> getAccountsByMatchId(Long matchId) {
//        Collection<AccountDto> dtos = new HashSet<>();
//        Collection<AccountMatch> accountMatches = accountMatchRepository.findAllByMatchId(matchId);
//        for (AccountMatch accountMatch : accountMatches) {
//            Account account = accountMatch.getAccount();
//            AccountDto accountDto = new AccountDto();
//
//            accountDto.setId(account.getId());
//            accountDto.setName(account.getName());
//            accountDto.setAge(account.getAge());
//            accountDto.setSex(account.getSex());
//            accountDto.setPhoneNumber(account.getPhoneNumber());
//            accountDto.setEmailAddress(account.getEmailAddress());
//            accountDto.setStreet(account.getStreet());
//            accountDto.setHouseNumber(account.getHouseNumber());
//            accountDto.setPostalCode(account.getPostalCode());
//            accountDto.setCity(account.getCity());
//            accountDto.setBio(account.getBio());
//            accountDto.setDocument(account.getDocument());
//            accountDto.setGivesHelp(account.isGivesHelp());
//            accountDto.setNeedsHelp(account.isNeedsHelp());
//            accountDto.setActivitiesToGive(account.getActivitiesToGive());
//            accountDto.setActivitiesToReceive(account.getActivitiesToReceive());
//            accountDto.setAvailability(account.getAvailability());
//            accountDto.setFrequency(account.getFrequency());
//
//            dtos.add(accountDto);
//        }
//        return dtos;
//    }
//
//    // Collection is de super klasse van zowel List als Set.
//    public Collection<MatchDto> getMatchesByAccountId(Long accountId) {
//        //We gebruiken hier Set om te voorkomen dat er dubbele entries in staan.
//        Set<MatchDto> dtos = new HashSet<>();
//        List<AccountMatch> accountMatches = accountMatchRepository.findAllByAccountId(accountId);
//        for (AccountMatch accountMatch : accountMatches) {
//            Match match = accountMatch.getMatch();
//            var dto = new MatchDto();
//
//            dto.setId(match.getId());
//            dto.setAccepted(match.isAccepted());
//            dto.setContactPerson(match.getContactPerson());
//            dto.setStartMatch(match.getStartMatch());
//            dto.setEndMatch(match.getEndMatch());
//            dto.setAvailability(match.getAvailability());
//            dto.setFrequency(match.getFrequency());
//
//            dtos.add(dto);
//        }
//        return dtos;
//    }
//
////    public MatchDto addAccountMatch(Long accountId1, Long accountId2) {
////        if (!accountRepository.existsById(accountId1) || !accountRepository.existsById(accountId2)) {
////            throw new RecordNotFoundException("One or more accounts not found.");
////        }
////
////        Account account1 = accountRepository.findById(accountId1).orElse(null);
////        Account account2 = accountRepository.findById(accountId2).orElse(null);
////
////        Match match = new Match();
////        // Set other match properties if needed
////        //matchbody vullen. adhv transferdto
////
////        AccountMatch accountMatch1 = new AccountMatch();
////        accountMatch1.setAccount(account1);
////        accountMatch1.setMatch(match);
////        AccountMatchKey id1 = new AccountMatchKey(accountId1, match.getId());
////        accountMatch1.setId(id1);
////        accountMatchRepository.save(match);
////
////        matchRepository.save(match);
////        return id1; // or id2, depending on your requirements
////    }
//}