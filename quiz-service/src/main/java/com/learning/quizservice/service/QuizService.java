package com.learning.quizservice.service;

import com.learning.quizservice.dao.QuizDao;
import com.learning.quizservice.feign.QuizInterface;
import com.learning.quizservice.model.QuestionWrapper;
import com.learning.quizservice.model.Quiz;
import com.learning.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();


        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionsForUser = quizInterface.getQuestionsFromId(questionIds);
        return questionsForUser;
    }

    public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) {
            ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
