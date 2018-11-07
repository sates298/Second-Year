Select film.title, language.name
From film Inner Join language  On film.language_id = language.language_id
Where film.description Like '%Documentary%';