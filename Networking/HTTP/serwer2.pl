use HTTP::Daemon;
use HTTP::Status;
#use IO::File;

my $d = HTTP::Daemon->new(
        LocalAddr => 'localhost',
        LocalPort => 1234,
    )|| die;

print "Please contact me at: <URL:", $d->url, ">\n";


while (my $c = $d->accept) {
   while (my $r = $c->get_request) {
       if ($r->method eq 'GET') {

         $res = HTTP::Response->new(1000);
         $res->content($r->headers_as_string);

         $c->send_response($res);
       }
       else {
           $c->send_error(RC_FORBIDDEN)
       }

   }
   $c->close;
   undef($c);
}
