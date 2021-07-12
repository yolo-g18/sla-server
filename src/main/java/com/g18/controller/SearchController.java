package com.g18.controller;

import com.g18.dto.EventDto;
import com.g18.dto.SearchFolderResponse;
import com.g18.dto.SearchRoomResponse;
import com.g18.entity.Event;
import com.g18.entity.Folder;
import com.g18.entity.Room;
import com.g18.service.EventService;
import com.g18.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping("/event")
    Page<EventDto> searchEvent(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchEvent(pageable,keySearch);
    }

    @GetMapping("/folder")
    Page<SearchFolderResponse> searchFolder(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchFolder(pageable ,keySearch);
    }

    @GetMapping("/room")
    Page<SearchRoomResponse> searchRoom(Pageable pageable, @RequestParam String keySearch){
        return searchService.searchRoom(pageable ,keySearch);
    }

}

