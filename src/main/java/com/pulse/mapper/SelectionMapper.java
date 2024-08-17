package com.pulse.mapper;

import com.pulse.dto.selection.FullSelectionResponse;
import com.pulse.dto.selection.ItemResponse;
import com.pulse.dto.selection.SelectionRequest;
import com.pulse.dto.selection.SelectionResponse;
import com.pulse.model.Selection;
import com.pulse.model.SelectionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

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
        selectionResponse.setNumberOfTalents((long) selection.getSelectionItems().size());
        selectionResponse.setClient(userMapper.mapToClientResponse(selection.getClient()));
        return selectionResponse;
    }

    public SelectionResponse mapToFullSelectionResponse(Selection selection){
        FullSelectionResponse fullSelectionResponse = new FullSelectionResponse();
        BeanUtils.copyProperties(selection, fullSelectionResponse);
        fullSelectionResponse.setNumberOfTalents((long) selection.getSelectionItems().size());
        fullSelectionResponse.setClient(userMapper.mapToClientResponse(selection.getClient()));
        fullSelectionResponse.setItems(
                selection.getSelectionItems().stream().map(
                        this::mapSelectionItemToItemResponse
                ).toList()
        );
        return fullSelectionResponse;
    }

    public ItemResponse mapSelectionItemToItemResponse(SelectionItem selectionItem) {
        ItemResponse itemResponse = new ItemResponse();
        BeanUtils.copyProperties(selectionItem, itemResponse);
        itemResponse.setSelectionStatus(selectionItem.getSelection().getStatus());
        itemResponse.setTalent(userMapper.mapToTalentResponse(selectionItem.getTalent()));
        return itemResponse;
    }
}
