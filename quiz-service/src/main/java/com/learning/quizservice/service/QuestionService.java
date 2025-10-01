package com.learning.quizservice.service;

import com.learning.quizapp.dao.QuestionDao;
import com.learning.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
