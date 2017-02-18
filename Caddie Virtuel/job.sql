/*
insert into reservations values(1, '123123', sysdate - interval '17' minute);-- 3minutes
insert into reservations values(1, 'aze5465', sysdate-interval '18' minute);
insert into reservations values(2, '123123', sysdate-interval '10' minute);--10 mintes
insert into reservations values(2, 'aze5465', sysdate-interval '13' minute);
commit;
delete from reservations where id_client IN(
select id_client from reservations
group by id_client
having max(date_Reservation) < sysdate - interval '15' minute);
*/

BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name             => 'CleanerJob',
   job_type             => 'PLSQL_BLOCK',
   job_action           => 'BEGIN 
   								delete from reservations 
   								where id_client IN (select id_client 
   													from reservations 
   													group by id_client 
   													having max(date_Reservation) < sysdate - interval ''15'' minute); 
                            	update appareils ap 
                            	set ETAT_SITUATION = (SELECT id_situation 
                            						  from etats_situation 
                            						  where description = ''WA'') 
                          		where ap.numero_serie NOT IN (select re.numero_serie from reservations re);
                            	commit; 
                            END;',
   repeat_interval      => 'FREQ=MINUTELY',
   start_date           =>  systimestamp + interval '10' second,
   enabled              =>  TRUE,
   comments             => 'Nettoie les caddies toutes les minutes');
END;
/