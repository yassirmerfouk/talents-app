package com.pulse.service.selection;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.selection.ItemRequest;
import com.pulse.dto.selection.ItemResponse;
import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;
import com.pulse.enumeration.SelectionStatus;
import com.pulse.enumeration.VerificationStatus;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.SelectionMapper;
import com.pulse.model.Client;
import com.pulse.model.Selection;
import com.pulse.model.SelectionItem;
import com.pulse.model.Talent;
import com.pulse.repository.SelectionItemRepository;
import com.pulse.repository.SelectionRepository;
import com.pulse.repository.TalentRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionServiceImpl implements SelectionService {

    private final SelectionRepository selectionRepository;
    private final SelectionItemRepository selectionItemRepository;

    private final SelectionMapper selectionMapper;

    private final AuthenticationService authenticationService;
    private final TalentRepository talentRepository;

    public Selection getSelection(Long id) {
        return selectionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Selection %d not found.", id))
        );
    }

    @Override
    public SelectionResponse addSelection(SelectionRequest selectionRequest) {
        authenticationService.checkVerification();
        List<Talent> talents = selectionRequest.getTalentsIds().stream().map(
                id -> {
                    Talent talent = talentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Talent %d not found.", id)));
                    if (talent.getStatus() != VerificationStatus.VERIFIED)
                        throw new CustomException(String.format("Talent %d not verified.", id));
                    return talent;
                }
        ).toList();
        Selection selection = selectionMapper.mapToSelection(selectionRequest);
        selection.setCreatedAt(LocalDateTime.now());
        selection.setStatus(SelectionStatus.CREATED);
        selection.setClient((Client) authenticationService.getAuthenticatedUser());
        selection.setSelectionItems(
                talents.stream().map(
                        talent -> SelectionItem.builder().selection(selection).talent(talent).build()
                ).toList()
        );
        selectionRepository.save(selection);
        return selectionMapper.mapToFullSelectionResponse(selection);
    }

    @Override
    public PageResponse<SelectionResponse> getSelections(
            String status, int page, int size
    ) {
        Page<Selection> selectionPage;
        if (status.equalsIgnoreCase("ALL"))
            selectionPage = selectionRepository.findAll(
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        else
            selectionPage = selectionRepository.findByStatus(
                    SelectionStatus.valueOf(status),
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        List<SelectionResponse> selections = selectionPage.getContent().stream()
                .map(selectionMapper::mapToSelectionResponse).toList();
        return new PageResponse<>(selections, page, size, selectionPage.getTotalPages(), selectionPage.getTotalElements());
    }

    @Override
    public PageResponse<SelectionResponse> getSelectionsByClientId(Long clientId, int page, int size) {
        Page<Selection> selectionPage = selectionRepository.findByClientId(
                clientId, PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        List<SelectionResponse> selections = selectionPage.getContent().stream()
                .map(selectionMapper::mapToSelectionResponse).toList();
        return new PageResponse<>(selections, page, size, selectionPage.getTotalPages(), selectionPage.getTotalElements());
    }

    @Override
    public PageResponse<SelectionResponse> getAuthenticatedClientSelections(
            int page, int size
    ) {
        return getSelectionsByClientId(authenticationService.getAuthenticatedUserId(), page, size);
    }

    @Override
    public SelectionResponse getSelectionById(Long id) {
        Selection selection = getSelection(id);
        if (!authenticationService.hasAuthority("ADMIN"))
            if (!selection.getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
                throw new ForbiddenException();
        return selectionMapper.mapToFullSelectionResponse(selection);
    }

    @Override
    public void acceptSelection(Long id) {
        Selection selection = getSelection(id);
        if (selection.getStatus() != SelectionStatus.CREATED)
            throw new CustomException("You can't accept this selection, status changed.");
        selection.setStatus(SelectionStatus.ACCEPTED);
        selectionRepository.save(selection);
    }

    @Override
    public void refuseSelection(Long id) {
        Selection selection = getSelection(id);
        if (selection.getStatus() != SelectionStatus.CREATED)
            throw new CustomException("You can't refuse this selection, status changed.");
        selection.setStatus(SelectionStatus.REFUSED);
        selectionRepository.save(selection);
    }

    @Override
    public void startChoosing(Long id) {
        Selection selection = getSelection(id);
        if (selection.getStatus() != SelectionStatus.ACCEPTED)
            throw new CustomException("You can't start choosing for this selection, status changed.");
        selection.setStatus(SelectionStatus.IN_CHOOSING);
        selectionRepository.save(selection);
    }

    @Override
    public void closeSelection(Long id) {
        Selection selection = getSelection(id);
        if (selection.getStatus() != SelectionStatus.IN_CHOOSING)
            throw new CustomException("You can't start close this selection, status changed.");
        selection.setStatus(SelectionStatus.CLOSED);
        selectionRepository.save(selection);
    }

    @Override
    public void deleteSelection(Long id) {
        Selection selection = getSelection(id);
        if (!selection.getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        if (selection.getStatus() != SelectionStatus.CREATED)
            throw new CustomException("You can't delete this selection, status changed.");
        selectionRepository.delete(selection);
    }

    @Override
    public void selectTalent(Long itemId){
        SelectionItem selectionItem = selectionItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Selection item not found."));
        if(!selectionItem.getSelection().getClient().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        if(selectionItem.getSelection().getStatus() != SelectionStatus.IN_CHOOSING)
            throw new CustomException("You can't select a talent, status changed.");
        selectionItem.setSelected(!selectionItem.isSelected());
        selectionItemRepository.save(selectionItem);
    }

    @Override
    public ItemResponse updateSelectionItem(Long id, ItemResponse itemResponse) {
        SelectionItem selectionItem = selectionItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Selection item not found."));
        selectionItem.copyProperties(
                SelectionItem.builder().level(itemResponse.getLevel()).report(itemResponse.getReport()).build()
        );
        selectionItemRepository.save(selectionItem);
        return selectionMapper.mapSelectionItemToItemResponse(selectionItem);
    }

    @Override
    public ItemResponse updateSelectionItem(Long id, ItemRequest itemRequest){
        SelectionItem selectionItem = selectionItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Selection item not found."));
        selectionItem.copyProperties(
                SelectionItem.builder().level(itemRequest.getLevel()).report(itemRequest.getReport()).build()
        );
        selectionItemRepository.save(selectionItem);
        return selectionMapper.mapSelectionItemToItemResponse(selectionItem);
    }


}
