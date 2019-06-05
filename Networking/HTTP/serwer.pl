use HTTP::Daemon;
use HTTP::Status;
#use IO::File;

my $d = HTTP::Daemon->new(
        LocalAddr => 'localhost',
        LocalPort => 4321,
    )|| die;

print "Please contact me at: <URL:", $d->url, ">\n";


while (my $c = $d->accept) {
   while (my $r = $c->get_request) {
       if ($r->method eq 'GET') {

         if ($r->url->path() eq '/'){
           $file_s= "./index.html";    # index.html - jakis istniejacy plik
           $c->send_file_response($file_s);
         } else {
           $c->send_file_response(".".$r->url->path());
         }

           $c->send_basic_header;
       }
       else {
           $c->send_error(RC_FORBIDDEN)
       }

   }
   $c->close;
   undef($c);
}
