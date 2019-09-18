'''
Created on 5 Sep 2019

@author: akash.verma
'''
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.remote_connection import RemoteConnection
from selenium.webdriver.remote.errorhandler import ErrorHandler
from selenium.webdriver.remote.file_detector import FileDetector, LocalFileDetector
from selenium.webdriver.remote import utils
import string
from selenium import webdriver
"""
class Duplicate(RemoteConnection):
    
    def __init__(self,session_id,server_addr):
        self.session_id = session_id;
        self.server_addr = server_addr;
        super(Duplicate,self).__init__(self.session_id,self.server_addr)
    
    def execute(self, command, params):
        command_info = self._commands[command]
        assert command_info is not None, 'Unrecognised command %s' % command
        path = string.Template(command_info[1]).substitute(params)
#         if hasattr(self, 'w3c') and self.w3c and isinstance(params, dict) and 'sessionId' in params:
        params['sessionId'] = '3cf0247912025af2e32e62e2d4b5273f'
        data = utils.dump_json(params)
        url = '%s%s' % (self._url, path)
        print(command_info[0])        
        return self._request(command_info[0], url, body=data)

driver = Duplicate('3cf0247912025af2e32e62e2d4b5273f', 'http://localhost:37410')
"""

class Duplicate(WebDriver):
    def __init__(self,sess_id,svr_addr):
        super(WebDriver,self).__init__();
        self.command_executor = RemoteConnection(svr_addr,True);
        self.session_id = sess_id;
        self.error_handler = ErrorHandler();
        self.file_detector = LocalFileDetector()
    
    #override
    def execute(self, driver_command, params=None):
        if self.session_id is not None:
            if not params:
                params = {'sessionId': self.session_id}
            elif 'sessionId' not in params:
                params['sessionId'] = self.session_id
        
        params = self._wrap_value(params)
        response = self.command_executor.execute(driver_command, params)
        if response:
            self.error_handler.check_response(response)
            response['value'] = self._unwrap_value(
                response.get('value', None))
            return response
        # If the server doesn't send a response, assume the command was
        # a success
        return {'success': 0, 'value': None, 'sessionId': self.session_id}
    # end execute
#end Duplicate

driver = Duplicate('43a2d508a3678a3867644b614acd9a90','http://localhost:22813')
driver.get("https://www.yahoo.in")
print("Akash Verma")