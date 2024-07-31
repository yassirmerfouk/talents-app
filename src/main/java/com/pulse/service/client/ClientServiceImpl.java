package com.pulse.service.client;

import com.pulse.dto.client.ClientRequest;
import com.pulse.dto.client.ClientResponse;
import com.pulse.dto.page.PageResponse;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.UserMapper;
import com.pulse.model.Client;
import com.pulse.repository.ClientRepository;
import com.pulse.repository.UserRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    @Override
    public ClientResponse profile(){
        Client client = (Client) authenticationService.getAuthenticatedUser();
        return userMapper.mapToClientResponse(client);
    }

    @Override
    public ClientResponse updateProfile(ClientRequest clientRequest){
        Client client = (Client) authenticationService.getAuthenticatedUser();
        if(userRepository.checkEmailAvailability(client.getId(), clientRequest.getEmail()))
            throw new CustomException("Email already used by another user.");
        client.copyProperties(userMapper.mapToClient(clientRequest));
        clientRepository.save(client);
        return userMapper.mapToClientResponse(client);
    }
    @Override
    public PageResponse<ClientResponse> getClients(String status, int page, int size){
        Page<Client> clientPage;
        if(!authenticationService.isAuthenticated()){
            clientPage = clientRepository.findByStatusAndEnabledTrue(VerificationStatus.VERIFIED,PageRequest.of(page,size));
        }else {
            if(authenticationService.hasAuthority("CLIENT") || authenticationService.hasAuthority("TALENT"))
                clientPage = clientRepository.findByStatusAndIdNotAndEnabledTrue(
                        VerificationStatus.VERIFIED, authenticationService.getAuthenticatedUserId(),
                        PageRequest.of(page,size)
                );
           else{
                if(status.equals("all"))
                    clientPage = clientRepository.findByEnabledTrue(PageRequest.of(page, size));
                else{
                    clientPage = clientRepository.findByStatusAndEnabledTrue(
                            VerificationStatus.valueOf(status),
                            PageRequest.of(page, size));
                }
            }
        }
        List<ClientResponse> clientResponses = clientPage.getContent().stream()
                .map(userMapper::mapToClientResponse).toList();
        return new PageResponse<>(clientResponses, page, size, clientPage.getTotalPages(), clientPage.getTotalElements());
    }

    @Override
    public ClientResponse getClientById(Long id){
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client %d not found.", id))
        );
        if(client.getStatus() != VerificationStatus.VERIFIED)
            if(!authenticationService.isAuthenticated() || !authenticationService.hasAuthority("ADMIN"))
                throw new ForbiddenException();
        return userMapper.mapToClientResponse(client);
    }

}
