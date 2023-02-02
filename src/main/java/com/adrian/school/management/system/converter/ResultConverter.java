package com.adrian.school.management.system.converter;

import com.adrian.school.management.system.dto.ExamResponse;
import com.adrian.school.management.system.dto.ResultResponse;
import com.adrian.school.management.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultConverter {

    private final ExamConverter examConverter;

    public ResultResponse convertResultToResultResponse(Result result) {
        ExamResponse examResponse = examConverter.convertExamToExamResponse(result.getExam());

        return new ResultResponse(result.getId(), result.getMark(), examResponse);
    }
}
