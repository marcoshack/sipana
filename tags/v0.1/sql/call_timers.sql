
SELECT
  DATE_FORMAT(FROM_UNIXTIME(startTime / 1000), '%Y-%m-%d %H:%i:%s') AS START_DATE,
  (setupTime - startTime) / 1000 AS SETUP_DELAY,
  (firstResponseTime - startTime) / 1000 AS FIRST_RESP_DELAY,
  (establishedTime - startTime) / 1000 AS ESTABL_DELAY,
  (endTime - establishedTime) / 1000 AS CALL_DURATION,
  fromUser,
  toUser
FROM
  sipana.sip_sessions 
WHERE
--  fromUser like 'asouza%' 
  method = 'INVITE'
  AND state = 3
  AND establishedTime > 0
  AND toUser not like '*%' -- Exclude PBX commands
ORDER BY
  START_DATE desc
