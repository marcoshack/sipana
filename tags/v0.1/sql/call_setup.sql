
SELECT
  DATE_FORMAT(FROM_UNIXTIME(startTime / 1000), '%Y-%m-%d %Hh') AS START_DATE,
  COUNT(*) AS CALLS,
  AVG((setupTime - startTime) / 1000) AS AVG_CALL_SETUP,
  MIN((setupTime - startTime) / 1000) AS MIN_CALL_SETUP,
  MAX((setupTime - startTime) / 1000) AS MAX_CALL_SETUP
FROM
  sipana.sip_sessions
WHERE
--  fromUser like 'asouza%' 
  method = 'INVITE'
  AND state IN (3)
  AND establishedTime > 0
  AND toUser not like '*%' -- Exclude PBX commands
GROUP BY
  START_DATE
