package com.hodolog.api.controller.opendata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hodolog.api.controller.opendata.BidApiResponse.Body;
import com.hodolog.api.controller.opendata.BidApiResponse.Header;
import com.hodolog.api.controller.opendata.BidApiResponse.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GenericObjectFileSaver {
    
    private final ObjectMapper objectMapper;
    
    public GenericObjectFileSaver() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * List<Object> items를 포함한 BidResponse를 JSON 파일로 저장
     */
    public void saveAsJson(BidApiResponse bidResponse, String directory, String filename) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        String fullPath = directory + File.separator + filename;
        objectMapper.writeValue(new File(fullPath), bidResponse);
        System.out.println("JSON 파일 저장 완료: " + fullPath);
    }
    
    /**
     * List<Object>를 CSV 파일로 저장
     * Object는 Map<String, Object> 형태로 변환되어 처리됩니다.
     */
    public void saveAsCsv(BidApiResponse bidResponse, String directory, String filename) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        String fullPath = directory + File.separator + filename;
        List<Object> items = bidResponse.getResponse().getBody().getItems();
        
        if (items == null || items.isEmpty()) {
            System.out.println("저장할 데이터가 없습니다.");
            return;
        }
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fullPath))) {
            // 첫 번째 아이템에서 헤더 추출
            Object firstItem = items.get(0);
            Map<String, Object> firstMap = objectToMap(firstItem);
            
            // CSV 헤더 작성
            boolean first = true;
            for (String key : firstMap.keySet()) {
                if (!first) writer.write(",");
                writer.write("\"" + key + "\"");
                first = false;
            }
            writer.newLine();
            
            // 데이터 작성
            for (Object item : items) {
                Map<String, Object> itemMap = objectToMap(item);
                first = true;
                
                for (String key : firstMap.keySet()) {
                    if (!first) writer.write(",");
                    
                    Object value = itemMap.get(key);
                    String valueStr = (value != null) ? value.toString() : "";
                    // CSV에서 쉼표와 따옴표 이스케이프 처리
                    valueStr = valueStr.replace("\"", "\"\"");
                    writer.write("\"" + valueStr + "\"");
                    first = false;
                }
                writer.newLine();
            }
        }
        
        System.out.println("CSV 파일 저장 완료: " + fullPath);
    }
    
    /**
     * List<Object>를 텍스트 파일로 저장 (JSON 형태로 예쁘게 출력)
     */
    public void saveAsText(BidApiResponse bidResponse, String directory, String filename) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        String fullPath = directory + File.separator + filename;
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fullPath))) {
            Response response = bidResponse.getResponse();
            Header header = response.getHeader();
            Body body = response.getBody();
            
            // 헤더 정보
            writer.write("=== 조회 결과 정보 ===");
            writer.newLine();
            writer.write("결과코드: " + (header.getResultCode() != null ? header.getResultCode() : ""));
            writer.newLine();
            writer.write("결과메시지: " + (header.getResultMsg() != null ? header.getResultMsg() : ""));
            writer.newLine();
            writer.write("총 건수: " + body.getTotalCount());
            writer.newLine();
            writer.write("페이지 번호: " + body.getPageNo());
            writer.newLine();
            writer.write("조회 건수: " + body.getNumOfRows());
            writer.newLine();
            writer.newLine();
            
            // 아이템 정보
            List<Object> items = body.getItems();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    writer.write("=== 아이템 " + (i + 1) + " ===");
                    writer.newLine();
                    
                    // Object를 JSON 문자열로 변환하여 예쁘게 출력
                    String jsonStr = objectMapper.writeValueAsString(items.get(i));
                    writer.write(jsonStr);
                    writer.newLine();
                    writer.newLine();
                }
            }
        }
        
        System.out.println("텍스트 파일 저장 완료: " + fullPath);
    }
    
    /**
     * 특정 필드만 추출하여 CSV로 저장
     */
    public void saveSelectedFieldsAsCsv(BidApiResponse bidResponse, String directory, String filename, String[] selectedFields) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        String fullPath = directory + File.separator + filename;
        List<Object> items = bidResponse.getResponse().getBody().getItems();
        
        if (items == null || items.isEmpty()) {
            System.out.println("저장할 데이터가 없습니다.");
            return;
        }
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fullPath))) {
            // 선택된 필드로 헤더 작성
            for (int i = 0; i < selectedFields.length; i++) {
                if (i > 0) writer.write(",");
                writer.write("\"" + selectedFields[i] + "\"");
            }
            writer.newLine();
            
            // 데이터 작성
            for (Object item : items) {
                Map<String, Object> itemMap = objectToMap(item);
                
                for (int i = 0; i < selectedFields.length; i++) {
                    if (i > 0) writer.write(",");
                    
                    Object value = itemMap.get(selectedFields[i]);
                    String valueStr = (value != null) ? value.toString() : "";
                    valueStr = valueStr.replace("\"", "\"\"");
                    writer.write("\"" + valueStr + "\"");
                }
                writer.newLine();
            }
        }
        
        System.out.println("선택된 필드 CSV 파일 저장 완료: " + fullPath);
    }
    
    /**
     * Object를 Map으로 변환하는 유틸리티 메소드
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> objectToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            // Jackson을 사용하여 Object를 Map으로 변환
            return objectMapper.convertValue(obj, Map.class);
        }
    }
    
    /**
     * List<Object>에서 사용 가능한 모든 필드명 추출
     */
    public void printAvailableFields(List<Object> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("데이터가 없습니다.");
            return;
        }
        
        System.out.println("=== 사용 가능한 필드 목록 ===");
        Map<String, Object> firstItemMap = objectToMap(items.get(0));
        int index = 1;
        for (String key : firstItemMap.keySet()) {
            System.out.println(index++ + ". " + key);
        }
    }
    
    /**
     * 파일명 자동 생성
     */
    public String generateFileName(String service, int pageNo, String inqryBgnDt, String inqryEndDt, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_%s_%s_%04d_%s.%s", 
          service, inqryBgnDt, inqryEndDt, pageNo, timestamp, extension);
    }
    
    /**
     * 모든 형식으로 저장하는 편의 메소드
     */
    public void saveAllFormats(BidApiResponse bidResponse, String directory, String baseFileName) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        saveAsJson(bidResponse, directory, baseFileName + "_" + timestamp + ".json");
        saveAsCsv(bidResponse, directory, baseFileName + "_" + timestamp + ".csv");
        saveAsText(bidResponse, directory, baseFileName + "_" + timestamp + ".txt");
        
        System.out.println("모든 형식의 파일 저장이 완료되었습니다.");
    }
}

// 사용 예시
class GenericObjectFileSaverExample {
    public static void main(String[] args) {
        try {
            // BidResponse 객체가 있다고 가정 (items는 List<Object>)
            BidApiResponse bidResponse = null; // 실제 데이터
            
            GenericObjectFileSaver fileSaver = new GenericObjectFileSaver();
            String directory = "./output";
            
            // 1. JSON으로 저장 (가장 안전한 방법)
            String jsonFileName = fileSaver.generateFileName("getBidData", 1, "20240101", "20240131", "json");
            fileSaver.saveAsJson(bidResponse, directory, jsonFileName);
            
            // 2. 모든 필드를 CSV로 저장
            String csvFileName = fileSaver.generateFileName("getBidData", 1, "20240101", "20240131", "csv");
            fileSaver.saveAsCsv(bidResponse, directory, csvFileName);
            
            // 3. 특정 필드만 선택하여 CSV로 저장
            String[] selectedFields = {
                "bidNtceNo", "bidNtceNm", "ntceInsttNm", "bidNtceDt", 
                "bidClseDt", "opengDt", "bdgtAmt", "presmptPrce"
            };
            String selectedCsvFileName = fileSaver.generateFileName("getBidData_selected", 1, "20240101", "20240131", "csv");
            fileSaver.saveSelectedFieldsAsCsv(bidResponse, directory, selectedCsvFileName, selectedFields);
            
            // 4. 텍스트로 저장
            String txtFileName = fileSaver.generateFileName("getBidData", 1, "20240101", "20240131", "txt");
            fileSaver.saveAsText(bidResponse, directory, txtFileName);
            
            // 5. 사용 가능한 필드 목록 출력
            if (bidResponse != null && bidResponse.getResponse().getBody().getItems() != null) {
                fileSaver.printAvailableFields(bidResponse.getResponse().getBody().getItems());
            }
            
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}