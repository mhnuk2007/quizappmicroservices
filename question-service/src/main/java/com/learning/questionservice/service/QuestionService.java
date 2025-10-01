package com.learning.questionservice.service;

import com.learning.questionservice.dao.QuestionDao;
import com.learning.questionservice.model.Question;
import com.learning.questionservice.model.QuestionWrapper;
import com.learning.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public List<Question> getAllQuestions() {
        return questionDao.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionDao.findByCategory(category);
    }

    public Optional<Question> getQuestionById(Integer id) {
        return questionDao.findById(id);
    }

    public void addQuestion(Question question) {
        questionDao.save(question);
    }

    public boolean updateQuestion(Integer id, Question updatedQuestion) {
        if (questionDao.existsById(id)) {
            updatedQuestion.setId(id); // ensure same ID
            questionDao.save(updatedQuestion);
            return true;
        }
        return false;
    }

    public boolean deleteQuestion(Integer id) {
        if (questionDao.existsById(id)) {
            questionDao.deleteById(id);
            return true;
        }
        return false;
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for (Integer questionId : questionIds) {
            questions.add(questionDao.findById(questionId).get());
        }
        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<Response> responses) {
        int right = 0;
        for (Response response : responses) {
            Question question = questionDao.findById(response.getId()).get();
            if (response.getResponse().equals(question.getRightAnswer())) {
                right++;
            }
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
