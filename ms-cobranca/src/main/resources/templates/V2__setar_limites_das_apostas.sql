/*ALTER TABLE public.jogo
    ADD COLUMN limite_aposta timestamp without time zone;
    
ALTER TABLE public.palpite
    ADD COLUMN limite_aposta timestamp without time zone;    

update jogo set limite_aposta = '2018-06-11 11:59:00';
update palpite set limite_aposta = '2018-06-11 11:59:00';
*/