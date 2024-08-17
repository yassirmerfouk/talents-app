package com.pulse.service.selection;

import com.pulse.dto.page.PageResponse;
import com.pulse.dto.selection.ItemResponse;
import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;

public interface SelectionService {
    SelectionResponse addSelection(SelectionRequest selectionRequest);

    PageResponse<SelectionResponse> getSelections(
            String status, int page, int size
    );

    PageResponse<SelectionResponse> getSelectionsByClientId(Long clientId, int page, int size);

    PageResponse<SelectionResponse> getAuthenticatedClientSelections(int page, int size);

    SelectionResponse getSelectionById(Long id);

    void acceptSelection(Long id);

    void refuseSelection(Long id);

    void startChoosing(Long id);

    void closeSelection(Long id);

    void deleteSelection(Long id);

    void selectTalent(Long itemId);

    ItemResponse updateSelectionItem(Long id, ItemResponse itemResponse);
}
