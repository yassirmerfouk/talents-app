package com.pulse.service.client;

import com.pulse.dto.client.ClientRequest;
import com.pulse.dto.client.ClientResponse;
import com.pulse.dto.page.PageResponse;

public interface ClientService {
    ClientResponse profile();
    ClientResponse updateProfile(ClientRequest clientRequest);
    PageResponse<ClientResponse> getClients(String status, int page, int size);

    ClientResponse getClientById(Long id);
}
