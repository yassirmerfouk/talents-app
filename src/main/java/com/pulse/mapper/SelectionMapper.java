package com.pulse.mapper;

import com.pulse.dto.selection.ItemResponse;
import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;
import com.pulse.model.Selection;
import com.pulse.model.SelectionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SelectionMapper {

    private final UserMapper userMapper;

    public Selection mapToSelection(SelectionRequest selectionRequest) {
        Selection selection = new Selection();
        BeanUtils.copyProperties(selectionRequest, selection);
        return selection;
    }

    public SelectionResponse mapToSelectionResponse(Selection selection) {
        SelectionResponse selectionResponse = new SelectionResponse();
        BeanUtils.copyProperties(selection, selectionResponse);
        selectionResponse.setItems(
                selection.getSelectionItems().stream().map(
                        this::mapSelectionItemToItemResponse
                ).toList()
        );
        return selectionResponse;
    }

    public ItemResponse mapSelectionItemToItemResponse(SelectionItem selectionItem) {
        ItemResponse itemResponse = new ItemResponse();
        BeanUtils.copyProperties(selectionItem, itemResponse);
        itemResponse.setTalent(userMapper.mapToTalentResponse(selectionItem.getTalent()));
        return itemResponse;
    }
}
