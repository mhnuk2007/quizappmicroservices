package com.learning.quizservice.service;

import com.learning.quizservice.dao.QuizDao;
import com.learning.quizservice.feign.QuizInterface;
import com.learning.quizservice.model.QuestionWrapper;
import com.learning.quizservice.model.Quiz;
import com.learning.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuizInterface quizInterface;


    public String createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ);

        if (questions == null || questions.isEmpty()) {
            return "Failed: No questions found for category " + category;
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return "Success";
    }

    public List<QuestionWrapper> getQuizQuestions(int id) {
        Quiz quiz = quizDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        List<Integer> questionIds = quiz.getQuestionIds();
        return quizInterface.getQuestionsFromId(questionIds);
    }

    public Integer calculateResult(int id, List<Response> responses) {
        // Verify quiz exists before calculating score
        quizDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        return quizInterface.getScore(responses);
    }
}