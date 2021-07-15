package com.g18.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.g18.dto.StudySetLearningDto;
import com.g18.dto.StudySetRequest;
import com.g18.dto.StudySetResponse;

import com.g18.entity.*;

import com.g18.model.Color;
import com.g18.model.Status;
import com.g18.model.UserStudySetId;
import com.g18.repository.*;

import com.g18.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class StudySetService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private CardLearningRepository cardLearningRepository;

    @Autowired
    private StudySetLearningRepository studySetLearningRepository;

    @Autowired
    private ExcelUtils excelUtils;


    public String createStudySet(StudySetRequest request) {
    	Long userId = authService.getCurrentAccount().getUser().getId();

        try{
            User user = userRepository.findById(userId).orElseThrow(()-> new ExpressionException("Study Set not exist"));

            StudySet studySet = new StudySet();
            studySet.setCreator(user);
            studySet.setDescription(request.getDescription());
            studySet.setTag(request.getTag());
            studySet.setTitle(request.getTitle());
            studySet.setPublic(request.isPublic());
            studySet.setCreatedDate(Instant.now());
            List<Card> listCard = request.getCards();
            for (Card card: listCard) {
                card.setStudySet(studySet);
            }
            studySet.setCards(listCard);
            return studySetRepository.save(studySet).getId().toString();
        }catch (Exception e){
            log.info(e.getMessage());
            return "add Study Set fail";
        }

    }

    public String deleteStudySet(Long studySetId) {
        StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(() ->new ExpressionException("Study Set not exist"));
        User auth = authService.getCurrentAccount().getUser();
        //Check permission
        if(auth.equals(studySet.getCreator())){
            studySetRepository.deleteById(studySetId);
            return "delete StudySet successfully";
        }else{
            return "Not permitted";
        }
    }

    public String editStudySet(StudySetRequest request) {
        Long userId = authService.getCurrentAccount().getUser().getId();

        try{
            User user = userRepository.findById(userId).orElse(null);

            StudySet studySet = studySetRepository.findById(request.getId())
                                        .orElseThrow(()->  new ExpressionException("Study Set not exist"));;
            if(user.equals(studySet.getCreator())){
                studySet.setDescription(request.getDescription());
                studySet.setTag(request.getTag());
                studySet.setTitle(request.getTitle());
                studySet.setPublic(request.isPublic());

                studySetRepository.save(studySet);
                return "update StudySet successfully";
            }else{
                return "Not permitted";
            }

        }catch (Exception e){
            log.info(e.getMessage());
            return "update Study Set fail";
        }
    }

    public ResponseEntity listStudySet() {
        User user = authService.getCurrentUser();
        try{
            List<StudySet> studySetList = userRepository.findById(user.getId()).orElse(null).getStudySetsOwn();
            List<StudySetResponse> responses = new ArrayList<>();
            for (StudySet studySet: studySetList) {
                StudySetLearning studySetLearning = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet,user);
                double progress = 0;
                if(studySetLearning != null) {
                    progress = studySetLearning.getProgress();
                }
                StudySetResponse studySetResponse = setStudySetResponse(studySet, progress);
                responses.add(studySetResponse);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity viewStudySetBy(Long id) {

        StudySet studySet = studySetRepository.findById(id)
                                    .orElseThrow(()->  new ExpressionException("Study Set not exist"));;
        User user = authService.getCurrentAccount().getUser();
        boolean isPublic = studySet.isPublic();
        if(!isPublic && !user.equals(studySet.getCreator())){
            return ResponseEntity.status(HttpStatus.OK).body("Not allowed");
        }
        StudySetResponse studySetResponse = setStudySetResponse(studySet, 0);
        return ResponseEntity.status(HttpStatus.CREATED).body(studySetResponse);
    }

    public String shareStudySetBy(StudySetRequest request) {
        // TODO Auto-generated method stub
        return "share StudySet successfully";
    }

    public void exportStudySetToExcel(HttpServletResponse response, Long studySetId) throws IOException {
        // TODO Auto-generated method stub
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=StudySetExport.xlsx";
        response.setHeader(headerKey, headerValue);

        HashMap<Integer,String> hashMapRowName = new HashMap<>();
        XSSFWorkbook workbook  = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("StudySet");
        excelUtils.writeHeaderLine(workbook,sheet,hashMapRowName);
        List<Card> cardList = studySetRepository.findById(studySetId).orElseThrow(()-> new ExpressionException("Not exist")).getCards();
        writeDataLines(workbook, sheet, cardList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private StudySetResponse setStudySetResponse(StudySet studySet, double progress){

        StudySetResponse studySetResponse = new StudySetResponse();
        studySetResponse.setStudySetId(studySet.getId());
        User creator = userRepository.findById(studySet.getCreator().getId()).orElseThrow(()->new ExpressionException("User not exist"));
        studySetResponse.setCreatorName(creator.getFirstName()+" "+creator.getLastName());
        studySetResponse.setUserId(creator.getId());
        studySetResponse.setDescription(studySet.getDescription());
        studySetResponse.setTag(studySet.getTag());
        studySetResponse.setTitle(studySet.getTitle());
        studySetResponse.setPublic(studySet.isPublic());
        studySetResponse.setNumberOfCard(studySet.getCards().size());
        studySetResponse.setProgress(progress);
        return studySetResponse;
    }

    private void writeDataLines(XSSFWorkbook workbook,XSSFSheet sheet, List<Card> cardList) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Card card : cardList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            excelUtils.createCell(sheet, row, columnCount++, card.getFront(), style);
            excelUtils.createCell(sheet, row, columnCount++, card.getBack(), style);
        }
    }

    public ResponseEntity getStudySetLearning(Long userId, Long studySetId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ExpressionException("User not exist"));
        StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(() -> new ExpressionException("Study Set not exist"));
        StudySetLearning studySetLearning = studySetLearningRepository.findStudySetLearningByStudySetAndUser(studySet, user);

        if(studySetLearning != null){
            boolean isPublic = studySetLearning.isPublic();
            if(!isPublic && !user.equals(studySet.getCreator())) {
                return ResponseEntity.status(HttpStatus.OK).body("Not allowed");
            }
            StudySetLearningDto response = new StudySetLearningDto();
            String creatorName = studySet.getCreator().getFirstName() + " " + studySet.getCreator().getLastName();
            response.setCreatorName(creatorName);
            response.setStudySetId(studySetId);
            response.setProgress(studySetLearning.getProgress());
            response.setStatus(studySetLearning.getStatus());
            response.setRating(studySetLearning.getRating());
            response.setFeedback(studySetLearning.getFeedback());
            response.setColor(studySetLearning.getColor());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date tempDate = Date.from(studySetLearning.getStartDate());
            String formattedDate = formatter.format(tempDate);
            response.setStartDate(formattedDate);

            tempDate = Date.from(studySetLearning.getExpectedDate());
            formattedDate = formatter.format(tempDate);
            response.setExpectedDate(formattedDate);
            response.setPublic(studySetLearning.isPublic());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("User has not started learning");
        }
    }
}
