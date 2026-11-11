INSERT INTO artistas (nome, bio, contato, verificado, criado_em) VALUES
('Artista de Rua Anonimo', 'Autoria coletiva dos muros do Beco do Batman, Vila Madalena.', NULL, FALSE, NOW(6)),
('Kobra', 'Muralista paulistano conhecido por murais coloridos em grande escala.', 'https://www.eduardokobra.com', TRUE, NOW(6));

INSERT INTO obras (titulo, ano, historia, audiodescricao, latitude, longitude, endereco, tipo, status, artista_id) VALUES
('Mural Colorido do Beco do Batman', 2018,
 'O Beco do Batman nasceu nos anos 1980 e virou galeria a ceu aberto, com paineis renovados por artistas de varios estilos.',
 'Parede alta coberta por formas geometricas em azul, laranja e rosa, com um rosto de olhos grandes ao centro.',
 -23.5558, -46.6869, 'Beco do Batman, Vila Madalena, Sao Paulo', 'MURAL', 'PUBLICADA', 1),
('Grafite do Olhar Atento', 2020,
 'Grafite de grande formato que retrata um olhar de perto, convidando o pedestre a observar a cidade.',
 'Grande olho desenhado em tons de cinza e dourado, ocupando todo o muro de esquina.',
 -23.5561, -46.6872, 'Rua Goncalo Afonso, Vila Madalena, Sao Paulo', 'GRAFITE', 'PUBLICADA', 1),
('Mural das Cores Vivas', 2017,
 'Painel que usa a tecnica de efeito tridimensional com cores intensas, tipico da obra do artista.',
 'Mural retangular com figura humana estilizada em faixas coloridas que lembram um prisma.',
 -23.5565, -46.6866, 'Rua Medeiros de Albuquerque, Vila Madalena, Sao Paulo', 'MURAL', 'PUBLICADA', 2),
('Escultura do Passante', 2022,
 'Escultura em metal instalada na praca, em homenagem aos pedestres do bairro.',
 'Figura humana de metal escuro, em tamanho real, caminhando com uma pasta na mao.',
 -23.5570, -46.6880, 'Praca Vila Madalena, Sao Paulo', 'ESCULTURA', 'EM_MODERACAO', 1);
