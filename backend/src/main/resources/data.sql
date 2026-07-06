INSERT INTO question (id, text, correct_answer_index, difficulty)
VALUES (1, 'Who won the FIFA World Cup 2018?', 1, 'NORMAL');

INSERT INTO question_options (question_id, option_text)
VALUES
(1, 'Brazil'),
(1, 'France'),
(1, 'Germany'),
(1, 'Argentina');


INSERT INTO question (id, text, correct_answer_index, difficulty)
VALUES (2, 'Which player has won the most Ballon d''Or awards?', 0, 'HARD');

INSERT INTO question_options (question_id, option_text)
VALUES
(2, 'Lionel Messi'),
(2, 'Cristiano Ronaldo'),
(2, 'Zinedine Zidane'),
(2, 'Ronaldinho');


INSERT INTO question (id, text, correct_answer_index, difficulty)
VALUES (3, 'What country won Euro 2020?', 0, 'EASY');

INSERT INTO question_options (question_id, option_text)
VALUES
(3, 'Italy'),
(3, 'England'),
(3, 'France'),
(3, 'Spain');