INSERT INTO contribuicoes (titulo, ano, historia, audiodescricao, latitude, longitude, endereco, tipo, artista_id, obra_id, status, motivo, criado_em) VALUES
('Mural da Esquina Azul', 2023,
 'Mural feito por moradores na esquina da Rua Harmonia, com tema de natureza urbana.',
 'Parede de esquina pintada de azul profundo, com folhas grandes em verde e amarelo.',
 -23.5552, -46.6860, 'Rua Harmonia, Vila Madalena, Sao Paulo', 'MURAL', 1, NULL, 'PENDENTE', NULL, NOW(6)),
('Grafite do Gato Curioso', 2021,
 'Grafite de um gato espiando por cima de um muro, muito fotografado pelos visitantes.',
 'Gato laranja com olhos verdes grandes, desenhado na parte de cima do muro, como se espiasse a rua.',
 -23.5559, -46.6875, 'Beco do Batman, Vila Madalena, Sao Paulo', 'GRAFITE', 1, NULL, 'PENDENTE', NULL, NOW(6)),
('Foto de pichacao no muro', NULL,
 'Sugestao enviada sem informacoes de autoria ou valor cultural.',
 NULL,
 -23.5600, -46.6900, 'Rua sem nome', 'GRAFITE', 1, NULL, 'REJEITADA',
 'Pichacao nao e arte autorizada e nao tem valor cultural comprovado.', NOW(6));
