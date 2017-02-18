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
                              where ap.numero_serie NOT IN (select re.numero_serie from reservations re UNION select it.numero_serie from items_facture it);
                              commit; 
                            END;',
   repeat_interval      => 'FREQ=MINUTELY',
   start_date           =>  systimestamp + interval '10' second,
   enabled              =>  TRUE,
   comments             => 'Nettoie les caddies toutes les minutes');
END;
/