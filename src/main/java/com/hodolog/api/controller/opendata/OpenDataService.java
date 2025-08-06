package com.hodolog.api.controller.opendata;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor    
public class OpenDataService {
  private final RestTemplate restTemplate;
    
  private static final String BASE_URL = "https://apis.data.go.kr/1230000/ad/BidPublicInfoService/";
  private static final String SERVICE_KEY = "rTloOuFWQ41vEEY9mj0NegdgXWYpZQeWgk9bH4KXK78O82wgPVjBtDRu0c/Ni244BThOLdzhdUMfhdeBBipwdQ==";
  private static final String TYPE = "json";
  private static final String NUM_OF_ROWS = "100";

  /**
     * API 호출하여 전체 페이지 데이터를 받아 파일로 저장
     */
    public void getBidPblancListInfoCnstwk(String inqryDiv, String inqryBgnDt, String inqryEndDt) {
      log.info("inqryBgnDt: {}", inqryBgnDt);
      log.info("inqryEndDt: {}", inqryEndDt);

      try {
          String service = "getBidPblancListInfoCnstwk";
          
          // 1페이지를 먼저 호출해서 totalCount를 확인
          BidApiResponse firstResponse = callApi(service, 1, inqryDiv, inqryBgnDt, inqryEndDt);
          if (firstResponse == null || firstResponse.getResponse() == null || 
              firstResponse.getResponse().getBody() == null) {
              throw new RuntimeException("API 응답이 비어있습니다.");
          }
          
          int totalCount = firstResponse.getResponse().getBody().getTotalCount();
          int numOfRows = Integer.parseInt(NUM_OF_ROWS);
          int totalPages = (int) Math.ceil((double) totalCount / numOfRows);
          
          log.info("전체 건수: {}, 페이지당 건수: {}, 총 페이지 수: {}", totalCount, numOfRows, totalPages);
          
          // 첫 번째 페이지 파일 저장
          saveFiles(firstResponse, service, 1, inqryBgnDt, inqryEndDt);
          log.info("페이지 {}/{} 완료", 1, totalPages);
          
          // 나머지 페이지들 순차적으로 처리
          for (int pageNo = 2; pageNo <= totalPages; pageNo++) {
              BidApiResponse response = callApi(service, pageNo, inqryDiv, inqryBgnDt, inqryEndDt);
              
              if (response != null && response.getResponse() != null && 
                  response.getResponse().getBody() != null) {
                  saveFiles(response, service, pageNo, inqryBgnDt, inqryEndDt);
                  log.info("페이지 {}/{} 완료", pageNo, totalPages);
                  
                  // API 호출 간격 조절 (서버 부하 방지)
                  Thread.sleep(100); // 100ms 대기
              } else {
                  log.warn("페이지 {} 응답이 비어있어 건너뜁니다.", pageNo);
              }
          }
          
          log.info("전체 {}개 페이지의 파일 생성이 완료되었습니다.", totalPages);
          
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.error("스레드 인터럽트 발생", e);
          throw new RuntimeException("데이터 처리 중 인터럽트가 발생했습니다: " + e.getMessage());
      } catch (Exception e) {
          log.error("API 호출 또는 파일 저장 중 오류 발생", e);
          throw new RuntimeException("데이터 처리 중 오류가 발생했습니다: " + e.getMessage());
      }
  }
  
  /**
  * API 호출
  */
  private BidApiResponse callApi(String service, int pageNo, String inqryDiv, String inqryBgnDt, String inqryEndDt) {
    String url = buildUrl(service, pageNo, inqryDiv, inqryBgnDt, inqryEndDt);
    // log.info("API 호출 - 페이지: {}, URL: {}", pageNo, url);
    try {
      ResponseEntity<BidApiResponse> response = restTemplate.getForEntity(url, BidApiResponse.class);
        return response.getBody();
    } catch (Exception e) {
      log.error("API 호출 실패 - 페이지: {}, URL: {}", pageNo, url, e);
      return null;
    }
  }

  /**
   * URL 생성
  */
  private String buildUrl(String service, int pageNo, String inqryDiv, String inqryBgnDt, String inqryEndDt) {
    return new StringBuilder(BASE_URL)
            .append(service)
            .append("?serviceKey=").append(SERVICE_KEY)
            .append("&type=").append(TYPE)
            .append("&numOfRows=").append(NUM_OF_ROWS)
            .append("&pageNo=").append(pageNo)
            .append("&inqryDiv=").append(inqryDiv)
            .append("&inqryBgnDt=").append(inqryBgnDt).append("0101")
            .append("&inqryEndDt=").append(inqryEndDt).append("2359")
            .toString();
  }

  private void saveFiles(BidApiResponse bidResponse, String service, int pageNo, String inqryBgnDt, String inqryEndDt) {
    try {
        // saveAsJsonFile(bidResponse, service, pageNo, inqryBgnDt, inqryEndDt, "json");
        saveAsTxtFile(bidResponse, service, pageNo, inqryBgnDt, inqryEndDt, "txt");
    } catch (IOException e) {
        log.error("페이지 {} 파일 저장 실패", pageNo, e);
        throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
    }
}

  /**
  * JSON 데이터를 파일로 저장
  */
  private void saveAsJsonFile(BidApiResponse bidResponse, String service, int pageNo, String inqryBgnDt, String inqryEndDt, String extension) throws IOException {
    GenericObjectFileSaver fileSaver = new GenericObjectFileSaver();
    String directory = "./output";
    
    String fileName = generateFileName(service, pageNo, inqryBgnDt, inqryEndDt, extension);
    fileSaver.saveAsJson(bidResponse, directory, fileName);    
  }
  
  /**
  * Txt 데이터를 파일로 저장
  */
  private void saveAsTxtFile(BidApiResponse bidResponse, String service, int pageNo, String inqryBgnDt, String inqryEndDt, String extension) throws IOException {
    GenericObjectFileSaver fileSaver = new GenericObjectFileSaver();
    String directory = "./output";
    
    String fileName = generateFileName(service, pageNo, inqryBgnDt, inqryEndDt, extension);
    fileSaver.saveAsText(bidResponse, directory, fileName);    
  }

  /**
   * 파일명 생성
   */
  private String generateFileName(String service, int pageNo, String inqryBgnDt, String inqryEndDt, String extension) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    return String.format("%s_%s_%s_%04d_%s.%s", 
      service, inqryBgnDt, inqryEndDt, pageNo, timestamp, extension);
  }
}
