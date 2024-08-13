package com.pulse.service.selection;

import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;
import com.pulse.model.SelectionItem;
import com.pulse.repository.SelectionItemRepository;
import com.pulse.repository.SelectionRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectionServiceImpl {

    private final SelectionRepository selectionRepository;
    private final SelectionItemRepository selectionItemRepository;

    private final AuthenticationService authenticationService;

    public SelectionResponse addSelection(SelectionRequest selectionRequest){
        return null;
    }

}
