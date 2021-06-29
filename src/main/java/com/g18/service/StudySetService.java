package com.g18.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g18.entity.Card;
import com.g18.entity.User;
import com.g18.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.StudySet;
import com.g18.repository.StudySetRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class StudySetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    public String createStudySet(ObjectNode json) {
    	Long userId = null;

        try {
        	userId = Long.parseLong(json.get("userId").asText());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        User user = userRepository.findById(userId).orElse(null);

        StudySet studySet = new StudySet();
        studySet.setCreator(user);
        studySet.setId(json.get("id").asLong());
        studySet.setDescription(json.get("description").asText());
        studySet.setTag(json.get("tag").asText());
        studySet.setTitle(json.get("title").asText());
        studySet.setPublic(json.get("public").asBoolean());

        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("listCard: "+json.get("listCard"));
            String jsonListCard = json.get("listCard").toString();
            List<Card> listCard = mapper.readValue(jsonListCard, new TypeReference<List<Card>>() {});
            studySet.setCards(listCard);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "add StudySet fail";
        }
        studySetRepository.save(studySet);
        return "add StudySet successfully";
    }

    public String deleteStudySet(Long id) {
        studySetRepository.deleteById(id);
        return "delete StudySet successfully";
    }

    public String editStudySet(ObjectNode json) {
        return "edit StudySet successfully";
    }


    public List<ObjectNode> listStudySet(ObjectNode json) {
        return null;
    }

    public ObjectNode viewStudySetBy(Long id) {
        return null;
    }


    public String shareStudySetBy(ObjectNode json) {
        // TODO Auto-generated method stub
        return "share StudySet successfully";
    }
}
