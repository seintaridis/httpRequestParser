SELECT ip
FROM http_request
WHERE req_date>='2017-01-01.13:00:00.0'
      AND req_date< '2017-01-01.14:00:00.0'
GROUP BY ip having count(*) > 100;